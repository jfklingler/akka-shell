resolvers += Resolver.url("bintray-sbt-plugin-releases",
  url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("me.lessis"      %  "bintray-sbt"            % "0.2.1")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin"  % "0.6.0")
