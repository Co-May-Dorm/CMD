pipeline {
	agent any
    stages {
        stage('Build') { 
            steps {

			try {
				sh 'mvn clean install'
				sh 'sudo systemctl enable cmd.service'
				sh 'sudo systemctl stop cmd'
				sh 'sudo systemctl start cmd'
				sh 'sudo systemctl status cmd'
				notifySuccessful()
			  } catch (e) {
				currentBuild.result = "FAILED"
				notifyFailed()
				throw e
			  }
            }
        }
    }
	def notifySuccessful() {
	  slackSend (color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

	  hipchatSend (color: 'GREEN', notify: true,
		  message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
		)

	  emailext (
		  subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
		  body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
			<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
		  recipientProviders: [[$class: 'DevelopersRecipientProvider']]
		)
	}
	def notifyFailed() {
		  slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")

		  hipchatSend (color: 'RED', notify: true,
			  message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
			)

		  emailext (
			  subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
			  body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
				<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
			  recipientProviders: [[$class: 'DevelopersRecipientProvider']]
			)
		}
}