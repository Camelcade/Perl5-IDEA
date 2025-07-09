/*
 * Copyright 2015-2025 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.psi.stubs

import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IElementType
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes.CLASS_ACCESSOR_METHOD
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.*
import fleet.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import java.io.IOException


private val LOG = Logger.getInstance(PerlPolynamedStubSerializingFactory::class.java)

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class PerlPolynamedStubSerializingFactory<Stub : PerlPolyNamedElementStub<Psi>, Psi : PerlPolyNamedElement<Stub>>(val elementType: IElementType) :
  StubSerializingElementFactory<Stub, Psi> {
  private companion object {
    val DIRECT_MAP = Object2IntOpenHashMap<IElementType>()
    val REVERSE_MAP: Int2ObjectOpenHashMap<IElementType> = Int2ObjectOpenHashMap()

    init {
      DIRECT_MAP.put(LIGHT_SUB_DEFINITION, 1)
      DIRECT_MAP.put(LIGHT_NAMESPACE_DEFINITION, 2)
      DIRECT_MAP.put(LIGHT_METHOD_DEFINITION, 3)
      DIRECT_MAP.put(CLASS_ACCESSOR_METHOD, 4)
      DIRECT_MAP.put(LIGHT_ATTRIBUTE_DEFINITION, 5)
      LOG.assertTrue(DIRECT_MAP.size == 5)

      DIRECT_MAP.forEach { (type, id) -> REVERSE_MAP.put(id.toInt(), type) }
    }
  }

  @Suppress("UNCHECKED_CAST")
  final override fun createStub(psi: Psi, parentStub: StubElement<out PsiElement>?): Stub {
    val lightNamedElements: MutableList<StubElement<*>> = ArrayList()
    val result: Stub = createStub(psi, parentStub, lightNamedElements)

    psi.lightElements.forEach { lightPsi ->
      val lightElementType = lightPsi.elementType
      val lightStubElement =
        (getStubSerializer(lightElementType) as StubElementFactory<StubElement<PsiElement>, PsiElement>).createStub(lightPsi, result)

      if (lightStubElement is PerlLightElementStub && lightPsi.isImplicit) {
        lightStubElement.setImplicit(true)
      }
      lightNamedElements.add(lightStubElement)
    }

    return result
  }

  /**
   * Creates the main psi element stub. the [lightElementsStubs] goingn to be populated with light elements stubs later, so it should be stored as is
   */
  protected abstract fun createStub(psi: Psi, parentStub: StubElement<*>?, lightElementsStubs: MutableList<StubElement<*>>): Stub

  private fun getStubSerializer(childStub: StubElement<*>): ObjectStubSerializer<*, *> {
    val elementType = childStub.elementType
    if (elementType is IStubElementType<*, *>) {
      return elementType
    }
    return StubElementRegistryService.getInstance().getStubSerializer(elementType)
      ?: throw IllegalArgumentException(
        "Don't know how to serialize:" + elementType + "; " + elementType.javaClass
    )
  }

  private fun getStubSerializer(elementType: IElementType): StubSerializer<*> =
    elementType as? IStubElementType<*, *> ?: StubElementRegistryService.getInstance().getStubSerializer(elementType) as StubSerializer<*>

  @Suppress("UNCHECKED_CAST")
  final override fun serialize(stub: Stub, dataStream: StubOutputStream) {
    val childrenStubs = stub.lightNamedElementsStubs.filter { it !is PerlLightElementStub || !it.isImplicit() }
    dataStream.writeVarInt(childrenStubs.size)
    serializeStub(stub, dataStream)

    for (childStub in childrenStubs) {
      dataStream.writeVarInt(getSerializationId(childStub)) // serialization id
      (getStubSerializer(childStub) as StubSerializer<StubElement<*>>).serialize(childStub, dataStream)
    }
  }

  @Suppress("UNCHECKED_CAST")
  final override fun indexStub(stub: Stub, sink: IndexSink) {
    stub.lightNamedElementsStubs.forEach { childStub ->
      (getStubSerializer(childStub) as StubSerializer<StubElement<*>>).indexStub(childStub, sink)
    }
    doIndexStub(stub, sink)
  }

  override fun shouldCreateStub(node: ASTNode): Boolean {
    val psi = node.psi
    assert(psi is PerlPolyNamedElement<*>)
    return !(psi as PerlPolyNamedElement<*>).lightElements.isEmpty()
  }

  final override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): Stub {
    val size = dataStream.readVarInt()
    val childStubs: MutableList<StubElement<*>> = ArrayList(size)
    val result: Stub = deserialize(dataStream, parentStub, childStubs)

    repeat(size) {
      childStubs.add(getStubSerializer(getElementTypeById(dataStream.readVarInt())).deserialize(dataStream, result))
    }

    return result
  }

  private fun getSerializationId(stubElement: StubElement<*>): Int {
    val id = DIRECT_MAP.getInt(stubElement.elementType)
    if (id > 0) {
      return id
    }
    throw IllegalArgumentException("Unregistered stub element class:" + stubElement.elementType)
  }

  private fun getElementTypeById(id: Int): IElementType =
    REVERSE_MAP[id] ?: throw IllegalArgumentException("Unregistered stub element id:$id")

  protected open fun doIndexStub(stub: Stub, sink: IndexSink): Unit = Unit

  // to save additional data in subclasses
  @Throws(IOException::class)
  protected open fun serializeStub(stub: Stub, dataStream: StubOutputStream): Unit = Unit

  override fun getExternalId(): String = "perl.poly.$elementType"

  @Throws(IOException::class)
  protected abstract fun deserialize(
    dataStream: StubInputStream,
    parentStub: StubElement<*>?,
    lightElementsStubs: MutableList<StubElement<*>>
  ): Stub

}