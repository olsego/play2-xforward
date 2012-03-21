import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2-xforward"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

	object Resolvers {
			// publish to my local github website clone, I will push manually
	        val crionicsRepository = Resolver.file("my local repo", new java.io.File(System.getProperty("user.home") + "/GitRepositories/orefalo.github.com/m2repo/releases/"))(Resolver.mavenStylePatterns) 
    }

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
			organization := "crionics",
			publishMavenStyle := true,
			publishTo := Some(Resolvers.crionicsRepository)
    )

}
