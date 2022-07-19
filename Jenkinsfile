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


}

