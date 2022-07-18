pipeline {
	agent any
    stages {
        stage('Build') { 
            steps {
				sh 'mvn clean install'
				sh 'sudo systemctl enable cmd.service'
				sh 'sudo systemctl stop cmd'
				sh 'sudo systemctl start cmd'
				sh 'sudo systemctl status cmd'
				notifySuccessful()
            }
        }
	}
	post {
		always{
			archiveArtifacts artifacts: '*.csv', onlyIfSuccessful: true
			
			emailext to: "nguyenminhdungtd98@gmail.com",
			subject: "jenkins build:${currentBuild.currentResult}: ${env.JOB_NAME}",
			body: "${currentBuild.currentResult}: Job ${env.JOB_NAME}\nMore Info can be found here: ${env.BUILD_URL}",
			attachmentsPattern: '*.csv',
			attachLog: true
			
		cleanWs()
		}
	}

}