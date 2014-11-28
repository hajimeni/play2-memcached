import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName         = "play2-memcached"
  val appVersion      = "0.6.0-H-SNAPSHOT"
  val appScalaVersion = "2.10.4"
  val appScalaCrossVersions = appScalaVersion :: "2.11.1" :: Nil

  lazy val baseSettings = Seq(
    scalaVersion := appScalaVersion,
    crossScalaVersions := appScalaCrossVersions,
    parallelExecution in Test := false
  )

  lazy val root = Project("root", base = file("."))
    .settings(baseSettings: _*)
    .settings(
      publishLocal := {},
      publish := {}
    ).aggregate(plugin)

  lazy val plugin = Project(appName, base = file("plugin"))
    .settings(baseSettings: _*)
    .settings(
      resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases/",
      resolvers += "Spy Repository" at "http://files.couchbase.com/maven2",
      libraryDependencies += "net.spy" % "spymemcached" % "2.9.0",
      libraryDependencies += "com.typesafe.play" %% "play" % play.core.PlayVersion.current % "provided",
      libraryDependencies += "com.typesafe.play" %% "play-cache" % play.core.PlayVersion.current % "provided",
      organization := "com.github.mumoshu",
      version := appVersion,
      publishTo <<= version { v: String =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
        else                             Some("releases" at nexus + "service/local/staging/deploy/maven2")
      },
      publishMavenStyle := true,
      publishArtifact in Test := false,
      pomIncludeRepository := { _ => false },
      pomExtra := (
        <url>https://github.com/mumoshu/play2-memcached</url>
          <licenses>
            <license>
              <name>BSD-style</name>
              <url>http://www.opensource.org/licenses/bsd-license.php</url>
              <distribution>repo</distribution>
            </license>
          </licenses>
          <scm>
            <url>git@github.com:mumoshu/play2-memcached.git</url>
            <connection>scm:git:git@github.com:mumoshu/play2-memcached.git</connection>
          </scm>
          <developers>
            <developer>
              <id>you</id>
              <name>KUOKA Yusuke</name>
              <url>https://github.com/mumoshu</url>
            </developer>
          </developers>
        )
    )

}
