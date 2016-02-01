//import com.typesafe.sbt.packager.Keys._

name := "login"

version := "0.0.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.gu" %% "pan-domain-auth-play_2-4-0" % "0.2.7",
  "com.amazonaws" % "aws-java-sdk" % "1.10.50"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator


sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

lazy val mainProject = project.in(file("."))
  .enablePlugins(PlayScala, RiffRaffArtifact)
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(
    // Never interested in the version number in the artifact name
    packageName in Universal := normalizedName.value,
    riffRaffPackageType := (packageZipTarball in config("universal")).value,
    riffRaffArtifactResources ++= Seq(
      baseDirectory.value / "cloudformation" / "login-tool.json" ->
        "packages/cloudformation/login-tool.json"
    )
  )
