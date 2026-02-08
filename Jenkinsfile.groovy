pipeline {
    agent any

    parameters {
        choice(name: 'Deploy_Env', choices: ['Staging', 'Dev', 'QA'], description: 'Choose the Enviroment where you wanted to Deploy')
        string(name: 'BRANCH', defaultValue: 'main', description: 'Git Branch' )
    }

    environment  {
        APP_NAME = 'notification'
        ENV = 'PROD'
        PROJECT = 'kaytm-insurance'
        SUB_PROJECT = 'brokerage'
        TECH_TEAM = 'DevOps'
        
    }

    stages {
        stage ('This is a clone stage') {
            steps {
                git branch: "${params.BRANCH}",
                    url: 'git@github.com:rawatdevendra198/jenkins-pipeline-as-code.git'
            }
        }

        stage ('Get the Commit ID') {
            steps {
                script {
                    env.COMMIT_ID = sh (
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()
                }
                 echo "Commit ID = ${env.COMMIT_ID}"
            }
        }

        stage ('This is build stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage ('Unit Test Stage') {
            steps {
                sh 'mvn test'
            }
        }

        stage ('Create Jar') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage ('Sonaqube Scan of the Artifact') {
            steps {
            sh 'mvn sonar:sonar'

            }

        }

        stage ('Artifact Upload') {
            steps {
                sh 'cp target/app.war /opt/tomcat/webapps/'
             
            }
        }
    }
}