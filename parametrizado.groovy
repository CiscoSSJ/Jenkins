job("ejemplo-DSL") {
	description("Job DSL de ejemplo para Jenkins")
  	scm {
      git("https://github.com/macloujulian/jenkins.job.parametrizado.git", "main") { node ->
        node / gitConfigName("cisco")
        node / gitConfigEmail ("ciscocarande@gmail.com")
     }
    }
     parameters {
       stringParam("nombre", defaultValue = "Cisco", description = "Parametro de cadena para el Job")
        booleanParam('agente', false)
        choiceParam('planeta', ['Tierra', 'Venus', 'Jupiter'])
    }
  	triggers {
  		cron ("H/7 * * * *")
  	}
  	steps {
 		shell("bash jobscript.sh")
    }
  	publishers {
        mailer("ciscocarande@gmail.com", true, true)
    }
 }
