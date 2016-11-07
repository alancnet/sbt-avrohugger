package sbtavrohugger
package formats
package standard

import GeneratorTask.caseClassGeneratorTask
import AvrohuggerSettings.{
  avroScalaCustomTypes,
  avroScalaCustomNamespace,
  avroScalaCustomEnumStyle
}

import java.io.File

import sbt.Keys._
import sbt.{
  Compile,
  Configuration,
  Setting,
  Task,
  TaskKey,
  inConfig
}

object AvroSettings  {

  val generate = TaskKey[Seq[File]]("generate",
    "Generate Scala sources for the Avro files.")

  def getSettings(
    avroConfig: Configuration,
    inputDir: Setting[File],
    outputDir: Setting[File],
    classPath: Setting[Task[Classpath]]): Seq[Setting[_]] = {

    inConfig(avroConfig)(Seq[Setting[_]](
    inputDir,
    outputDir,
    classPath,
    avroScalaCustomTypes in avroConfig := Map.empty[String, Class[_]],
    avroScalaCustomNamespace in avroConfig := Map.empty[String, String],
    avroScalaCustomEnumStyle in avroConfig := Map.empty[String, String],
    generate <<= caseClassGeneratorTask(avroConfig))) ++ Seq[Setting[_]](
      sourceGenerators in Compile <+= (generate in avroConfig),
      managedSourceDirectories in Compile <+= (scalaSource in avroConfig),
      cleanFiles <+= (scalaSource in avroConfig),
      ivyConfigurations += avroConfig)
  }

}
