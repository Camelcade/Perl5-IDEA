# stool samples: currently unsupported and unparsable examples

#  $desc =~ s/{/\\{/g;
#$rest=~/=> {$/
#$text =~ s/{/{{/g;
#    s/\bIM_ROUND\(/IM_ROUND_8(/g;
#m!\G(.*?<%[ ]*(?:\$(?:\.|self->))fl(?:oc)?(?:[ ]*\((.*?)\))?[ ]*{[ ]*%>(.*?)</%>)!sg
#    return '" . ' . join('.', map {s/\\\\#\\{/#\\{/; $_} @parts) . '."';

# causes LexerEditorHighlighter exception
#if ($str =~ s{($finder_regexp)([\[\{]?)}
#    <
#        if ($1 =~ tr/\{//) { substr($1, 0, ($1=~tr/#//)+1) . "{$replacement}$2" }
#        else              { substr($1, 0, ($1=~tr/#//)+1) . "$replacement$2" }
#    >ge
#    )
# {
#
#    # TODO do this without breaking encapsulation!
#    $node->{content} = $str;
#}

# DBIx::LimitDialects.pm semi bug DBIHacks.pm ResultSet.pm

# block undistinctable from anon hash, cause such hash is a valid block
#sub blah {
#  { 1 => 0 # SQL_CB_NULL
#  , 2 => 1 # SQL_CB_NON_NULL
#  }->{$_[0]->{ado_conn}->Properties->{'NULL Concatenation Behavior'}{Value}};
#}

# heredoc with empty marker
#__PACKAGE__->set_sql(Retrieve => <<'');
#SELECT __ESSENTIAL__
#FROM   __TABLE__
#WHERE  %s

# my $digest = new $hash(@param);

