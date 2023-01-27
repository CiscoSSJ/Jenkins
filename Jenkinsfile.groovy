podTemplate(inheritFrom: 'icagent01') {
  node(POD_LABEL) {
    stage('Clean Workspace') {
        container('jenkins-slave') {
            cleanWs()
        }
    }
    stage ('git checkout'){
        steps.echo ":: Checking out code"
		steps.checkout( [ $class: 'GitSCM',
                  branches: [ [ name: "${branch}" ] ],
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [],
                  gitTool: 'Default',
                  submoduleCfg: [],
                  userRemoteConfigs: [ [ credentialsId: "", url: "${params.repository}" ] ]
                ] )
		steps.echo "Getting GIT_COMMIT..."
		sh 'git rev-parse HEAD > commit'
        def commit = readFile('commit').trim()
        env.GIT_COMMIT = commit
        echo "GIT_COMMIT: ${env.GIT_COMMIT}"
    }
    stage('Info') {
        container('jenkins-slave') {
            sh 'pwd'
            sh 'ls'
        }
    }
    stage('Build') {
        container('maven') {
            sh 'mvn -B -DskipTests clean package'
        }
    }
    stage('Test') {
        container('maven') {
            sh 'mvn test'
        }
    }
    stage('Kubectl') {
        container('kubectl') {
            sh 'kubectl --version'
        }
    }
  }
}