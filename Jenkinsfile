pipeline {
	agent any
	environment {
        report = '/var/lib/jenkins/workspace/CMD-BE/Email/email-template.html'
    }
    stages {
        stage('Build') { 
            steps {
				sh 'mvn clean install'
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
    post {
       always {
			script {
                  def publisher = LastChanges.getLastChangesPublisher "PREVIOUS_REVISION", "SIDE", "LINE", true, true, "", "", "", "", ""
                  publisher.publishLastChanges()
                  def htmlDiff = publisher.getHtmlDiff()
                  writeFile file: 'build-diff.html', text: htmlDiff
                    emailext (
                      subject: "Jenkins - changes of ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                      attachmentsPattern: '**/*build-diff.html',
                      mimeType: 'text/html',
                      body: """<p>See attached diff of build <b>${env.JOB_NAME} #${env.BUILD_NUMBER}</b>.</p>
                        <p>Check build changes on Jenkins <b><a href="${env.BUILD_URL}/last-changes">here</a></b>.</p>""",
                      to: "YOUR-EMAIL@gmail.com" )
                }
       }
    }

}

