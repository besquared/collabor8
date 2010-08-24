import sbt._

class Collabor8(info: ProjectInfo) extends DefaultProject(info) {
  val codasRepo = "Coda's Repo" at "http://repo.codahale.com"
  val oracleRepo = "Oracle Maven Repo" at "http://download.oracle.com/maven/"
  val scalaToolsReleases = "scala-tools.org Releases" at "http://scala-tools.org/repo-releases"
  
  val bdb = "com.sleepycat" % "je" % "4.0.92" withSources()
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.5" % "test" withSources()
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0-SNAPSHOT" % "test" withSources()
}