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
            }
        }        
		stage('Deploy') { 
            steps {
				sh 'sudo systemctl enable cmd.service'
				sh 'sudo systemctl stop cmd'
				sh 'sudo systemctl start cmd'
				sh 'sudo systemctl status cmd'
            }
        }
	}
	environment {
        EMAIL_TO = 'nguyenminhdungtd98@gmail'
    }
	post {
        failure {
            emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}', 
                    to: "${EMAIL_TO}", 
                    subject: 'Build failed in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
        }
        unstable {
            emailext body: 'Check console output at $BUILD_URL to view the results. \n\n ${CHANGES} \n\n -------------------------------------------------- \n${BUILD_LOG, maxLines=100, escapeHtml=false}', 
                    to: "${EMAIL_TO}", 
                    subject: 'Unstable build in Jenkins: $PROJECT_NAME - #$BUILD_NUMBER'
        }
        always {
            emailext body: 'Check console output at $BUILD_URL to view the results.', 
                    to: "${EMAIL_TO}", 
                    subject: 'Jenkins build is back to normal: $PROJECT_NAME - #$BUILD_NUMBER'
        }
    }

}

