pipeline {
    agent any

    parameters {
        choice(name: 'Deploy_Env', choices ['Staging', 'Dev', 'QA'], description: 'Choose the Enviroment where you wanted to Deploy')
        choice(name: 'BRANCH', defaultValue: 'main')
    }

    enviroment {
        APP_NAME = '$(APP_NAME)'
        ENV = 'PROD'
        PROJECT = 'kaytm-insurance'
        SUB_PROJECT = 'brokerage'
        TECH_TEAM = 'DevOps'
        COMMIT_ID = ''
        
    }

    stages {
        stage ('This is a clone stage') {
            step {
                sh 

            }
        }
    }
}