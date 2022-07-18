pipeline {
	agent any
    stages {
        stage('Build') { 
            steps {
				sh 'sudo systemctl enable cmd-fe.service'
				sh 'sudo systemctl stop cmd-fe'
				sh 'sudo systemctl start cmd-fe'
				sh 'sudo systemctl status cmd-fe'
            }
        }
    }
}