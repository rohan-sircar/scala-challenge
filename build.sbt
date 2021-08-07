//format: off
val Http4sVersion          = "0.21.23"
val CirceVersion           = "0.13.0"
val MunitVersion           = "0.7.20"
val LogbackVersion         = "1.2.3"
val MunitCatsEffectVersion = "0.13.0"
val FlywayVersion          = "7.5.3"
val MonixVersion           = "3.4.0"
// val MonixBioVersion     = "1.1.0"
val MonixBioVersion        = "0a2ad29275"
// val MonixBioVersion        = "1.1.0+39-73a5fb1c-SNAPSHOT"
val SttpVersion            = "3.1.7"
// val OdinVersion            = "0.11.0"
val OdinVersion            = "97e004aa52"
val TestContainersVersion  = "0.39.3"
val PureconfigVersion      = "0.16.0"
val RefinedVersion         = "0.9.19"
val EnumeratumVersion      = "1.6.1"
val SlickVersion           = "3.3.3"
val SlickPgVersion         = "0.19.6"
val ChimneyVersion         = "0.6.1"
val TapirVersion           = "0.17.19"
val TsecVersion            = "0.2.1"
//format: on

scalaVersion in ThisBuild := "2.13.6"

resolvers in ThisBuild += "jitpack" at "https://jitpack.io"

lazy val DeepIntegrationTest = IntegrationTest.extend(Test)

lazy val root = (project in file("."))
  .configs(DeepIntegrationTest)
  .settings(
    fork := true,
    organization := "wow.doge",
    name := "scala-challenge",
    Defaults.itSettings,
    inConfig(DeepIntegrationTest)(scalafixConfigSettings(DeepIntegrationTest)),
    buildInfoPackage := "wow.doge.app",
    fork in DeepIntegrationTest := true,
    envVars in DeepIntegrationTest := Map("PROJECT_ENV" -> "test"),
    fork in Test := true,
    envVars in Test := Map("PROJECT_ENV" -> "test"),
    libraryDependencies ++= Seq(
      //format: off
      "io.circe"                      %% "circe-generic"             % CirceVersion,
      "io.circe"                      %% "circe-generic-extras"      % CirceVersion,
      // "co.fs2"                        %% "fs2-reactive-streams"      % "2.5.0",
      // "io.monix"                      %% "monix-bio"                 % MonixBioVersion,
      "com.github.monix"               % "monix-bio"                 % MonixBioVersion,
      // "com.github.valskalla"          %% "odin-monix"                % OdinVersion,
      "com.github.rohan-sircar.odin"  %% "odin-monix"                % OdinVersion,
      "com.github.rohan-sircar.odin"  %% "odin-slf4j"                % OdinVersion,
      "com.github.rohan-sircar.odin"  %% "odin-json"                 % OdinVersion,
      "com.github.rohan-sircar.odin"  %% "odin-extras"               % OdinVersion,
      "com.beachape"                  %% "enumeratum"                % EnumeratumVersion,
      "com.beachape"                  %% "enumeratum-circe"          % EnumeratumVersion,
      "com.beachape"                  %% "enumeratum-cats"           % EnumeratumVersion,
      "eu.timepit"                    %% "refined-cats"             % RefinedVersion,
      "io.circe"                      %% "circe-fs2"                 % CirceVersion,
      "io.circe"                      %% "circe-refined"             % CirceVersion,
      "io.estatico"                   %% "newtype"                   % "0.4.4",
      "com.softwaremill.sttp.client3" %% "core"                      % SttpVersion,
      "com.softwaremill.sttp.client3" %% "monix"                     % SttpVersion,
      "com.softwaremill.sttp.client3" %% "fs2"                       % SttpVersion,
      "com.softwaremill.sttp.client3" %% "circe"                     % SttpVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-cats"                % TapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-enumeratum"          % TapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-json-circe"          % TapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-refined"             % TapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-newtype"             % TapirVersion,
      "com.softwaremill.sttp.tapir"   %% "tapir-http4s-server"       % TapirVersion,
      "co.fs2"                        %% "fs2-reactive-streams"      % "2.5.0",
      "org.http4s"                    %% "http4s-ember-server"       % Http4sVersion,
      "org.http4s"                    %% "http4s-ember-client"       % Http4sVersion,
      "org.http4s"                    %% "http4s-dropwizard-metrics" % Http4sVersion,
      "org.http4s"                    %% "http4s-circe"              % Http4sVersion,
      "org.http4s"                    %% "http4s-dsl"                % Http4sVersion,
      "io.circe"                      %% "circe-generic"             % CirceVersion,
      "io.monix"                      %% "monix"                     % MonixVersion,
      "com.softwaremill.quicklens"    %% "quicklens"                 % "1.6.1",
      "com.typesafe.scala-logging"    %% "scala-logging"             % "3.9.2",
      "com.lihaoyi"                   %% "os-lib"                    % "0.7.1",
      "com.chuusai"                   %% "shapeless"                 % "2.3.3",
      "com.lihaoyi"                   %% "sourcecode"                % "0.2.1",
      "io.scalaland"                  %% "chimney"                   % "0.6.1",
      "io.scalaland"                  %% "chimney-cats"              % "0.6.1",
      "io.estatico"                   %% "newtype"                   % "0.4.4",
      "jp.ne.opt"                     %% "chronoscala"               % "1.0.0",
      "com.lihaoyi"                   %% "pprint"                   % "0.6.6",
      "io.chrisdavenport"             %% "fuuid"                    % "0.6.0",
      "io.chrisdavenport"             %% "fuuid-circe"              % "0.6.0",
      //test deps
      "org.scalameta"                 %% "munit"                           % MunitVersion          % "it,test",
      "de.lolhens"                    %% "munit-tagless-final"             % "0.0.1"               % "it,test",
      "org.scalameta"                 %% "munit-scalacheck"                % "0.7.23"              % "it,test",
      "org.scalacheck"                %% "scalacheck"                      % "1.15.3"              % "it,test",
      "com.dimafeng"                  %% "testcontainers-scala-munit"      % TestContainersVersion % DeepIntegrationTest,
      "com.dimafeng"                  %% "testcontainers-scala-postgresql" % TestContainersVersion % DeepIntegrationTest
      //format: on
    ),
    testFrameworks += new TestFramework("munit.Framework"),
    buildInfoKeys := Seq[BuildInfoKey](
      name,
      version,
      scalaVersion,
      sbtVersion,
      libraryDependencies,
      javacOptions,
      dockerBaseImage
    ),
    wartremoverErrors in (Test, compile) ++= WartRemoverErrors,
    wartremoverErrors in (DeepIntegrationTest, compile) ++= WartRemoverErrors,
    wartremoverErrors in (Compile, compile) ++= WartRemoverErrors,
    wartremoverExcluded += (sourceManaged in Compile).value
  )

inThisBuild(
  List(
    semanticdbEnabled := true, // enable SemanticDB
    semanticdbVersion := "4.4.18", // use Scalafix compatible version
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    dynverSeparator := "-",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-deprecation",
      "-feature",
      "-language:existentials",
      "-language:experimental.macros",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xlint",
      "-Ywarn-numeric-widen",
      "-Ymacro-annotations",
      //silence warnings for by-name implicits
      "-Wconf:cat=lint-byname-implicit:s",
      //give errors on non exhaustive matches
      "-Wconf:msg=match may not be exhaustive:e",
      """-Wconf:site=wow\.doge\.http4sdemo\.slickcodegen\..*:i""",
      // """-Wconf:src=target/src_managed/Tables.scala:s""",
      "-explaintypes", // Explain type errors in more detail.
      "-Vimplicits",
      "-Vtype-diffs"
    ),
    scalacOptions ++= {
      if (insideCI.value) Seq("-Xfatal-warnings")
      else Seq.empty
    },
    javacOptions ++= Seq("-source", "11", "-target", "11"),
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.3",
    skip in publish := true
  )
)

// libraryDependencies ++= Seq(
//   "org.scalameta" %% "svm-subs" % "20.2.0" % "compile-internal"
// )

addCommandAlias("lint-check", "scalafmtCheckAll; scalafixAll --check")
addCommandAlias("lint-run", "scalafmtAll; scalafixAll")

val WartRemoverErrors =
  Warts.allBut(
    Wart.Any,
    Wart.NonUnitStatements,
    Wart.StringPlusAny,
    Wart.Overloading,
    Wart.PublicInference,
    Wart.Nothing,
    Wart.Var,
    Wart.DefaultArguments,
    Wart.OptionPartial,
    // Wart.MutableDataStructures,
    Wart.ImplicitConversion,
    Wart.ImplicitParameter,
    Wart.ToString,
    Wart.Recursion,
    Wart.While,
    Wart.ExplicitImplicitTypes,
    Wart.ListUnapply
  )
