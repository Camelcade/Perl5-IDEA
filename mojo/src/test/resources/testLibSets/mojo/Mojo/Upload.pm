package Mojo::Upload;
use Mojo::Base -base;

has [qw(asset filename headers name)];

sub move_to { $_[0]->asset->move_to($_[1]) and return $_[0] }

sub size  { shift->asset->size }
sub slurp { shift->asset->slurp }

1;

=encoding utf8

=head1 NAME

Mojo::Upload - Upload

=head1 SYNOPSIS

  use Mojo::Upload;

  my $upload = Mojo::Upload->new;
  say $upload->filename;
  $upload->move_to('/home/sri/foo.txt');

=head1 DESCRIPTION

L<Mojo::Upload> is a container for uploaded files.

=head1 ATTRIBUTES

L<Mojo::Upload> implements the following attributes.

=head2 asset

  my $asset = $upload->asset;
  $upload   = $upload->asset(Mojo::Asset::File->new);

Asset containing the uploaded data, usually a L<Mojo::Asset::File> or
L<Mojo::Asset::Memory> object.

=head2 filename

  my $filename = $upload->filename;
  $upload      = $upload->filename('foo.txt');

Name of the uploaded file.

=head2 headers

  my $headers = $upload->headers;
  $upload     = $upload->headers(Mojo::Headers->new);

Headers for upload, usually a L<Mojo::Headers> object.

=head2 name

  my $name = $upload->name;
  $upload  = $upload->name('foo');

Name of the upload.

=head1 METHODS

L<Mojo::Upload> inherits all methods from L<Mojo::Base> and implements the
following new ones.

=head2 move_to

  $upload = $upload->move_to('/home/sri/foo.txt');

Move uploaded data into a specific file.

=head2 size

  my $size = $upload->size;

Size of uploaded data in bytes.

=head2 slurp

  my $bytes = $upload->slurp;

Read all uploaded data at once.

=head1 SEE ALSO

L<Mojolicious>, L<Mojolicious::Guides>, L<https://mojolicious.org>.

=cut
