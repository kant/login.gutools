//import com.typesafe.sbt.packager.Keys._

name := "login"

version := "0.0.1"

val awsSdkVersion = "1.10.72"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.gu" %% "pan-domain-auth-play_2-4-0" % "0.2.13",
  "com.amazonaws" % "aws-java-sdk-ec2" % awsSdkVersion,
  "com.amazonaws" % "aws-java-sdk-cloudwatch" % awsSdkVersion,
  "com.amazonaws" % "aws-java-sdk-dynamodb" % awsSdkVersion,
  "com.amazonaws" % "aws-java-sdk-s3" % awsSdkVersion,
  "com.amazonaws" % "aws-java-sdk" % awsSdkVersion,
  "com.github.nscala-time" %% "nscala-time" % "2.12.0",
  "io.megl" %% "play-json-extra" % "2.4.3",
  "com.github.t3hnar" % "scala-bcrypt_2.11" % "2.6",
  "com.gu" %% "scanamo" % "0.4.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalaVersion := "2.11.8"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator


sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

lazy val mainProject = project.in(file("."))
  .enablePlugins(PlayScala, RiffRaffArtifact)
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(addCommandAlias("devrun", "run -Dconfig.resource=application.local.conf 9000"): _*)
  .settings(
    // Never interested in the version number in the artifact name
    packageName in Universal := normalizedName.value,
    riffRaffPackageName := s"editorial-tools:${name.value}",
    riffRaffManifestProjectName := riffRaffPackageName.value,
    riffRaffBuildIdentifier :=  Option(System.getenv("CIRCLE_BUILD_NUM")).getOrElse("dev"),
    riffRaffUploadArtifactBucket := Option("riffraff-artifact"),
    riffRaffUploadManifestBucket := Option("riffraff-builds"),
    riffRaffManifestBranch := Option(System.getenv("CIRCLE_BRANCH")).getOrElse("dev"),
    riffRaffPackageType := (packageZipTarball in config("universal")).value,
    riffRaffArtifactResources ++= Seq(
      riffRaffPackageType.value -> s"packages/${name.value}/${name.value}.tgz",
      baseDirectory.value / "cloudformation" / "login-tool.json" ->
        "packages/cloudformation/login-tool.json"
    ))
