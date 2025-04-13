pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'mail-archive/mail-archive'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    tools {
        maven 'Maven 3.6.3'
        jdk 'JDK 8'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.example.com', 'docker-registry-credentials') {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push('latest')
                    }
                }
            }
        }
        
        stage('Deploy to Dev') {
            steps {
                sh """
                    docker stop mail-archive || true
                    docker rm mail-archive || true
                    docker run -d --name mail-archive \
                        -p 8080:8080 \
                        -v /data/attachments:/data/attachments \
                        -e SPRING_PROFILES_ACTIVE=dev \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                """
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
            emailext (
                subject: "Pipeline Failed: ${currentBuild.fullDisplayName}",
                body: "Something is wrong with ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
    }
} 