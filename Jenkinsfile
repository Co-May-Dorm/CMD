pipeline {
	agent any
	environment {
        report = '/var/lib/jenkins/workspace/CMD-BE/email-template.html'
    }
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
		stage('save log build') {
			steps {
				script {
					def logContent = Jenkins.getInstance()
					.getItemByFullName(env.JOB_NAME)
					.getBuildByNumber(
					Integer.parseInt(env.BUILD_NUMBER))
					.logFile.text
					// copy the log in the job's own workspace
					writeFile file: "buildlog.txt", text: logContent
				}
			}
		}
	}
    post {
       always {
            script {
                html_body = sh(script: "cat ${report}", returnStdout: true).trim()
                emailext body: "$html_body", attachLog: true, 
                subject: '$PROJECT_NAME - Build#$BUILD_NUMBER - $BUILD_STATUS!', 
                to: 'nguyenminhdungtd98@gmail.com',
                mimeType: 'text/html'
            }
       }
    }

}

