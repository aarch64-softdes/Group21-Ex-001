pipeline {
    agent any
    
    tools {
        jdk 'JDK 21'
        maven 'Maven 3.9.6'
    }

    environment {
        SONAR_TOKEN = credentials('sonar_token')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                dir('backend') {
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Test') {
            steps {
                dir('backend') {
                    sh 'mvn verify'
                }
            }
            post {
                always {
                    dir('backend') {
                        junit '**/target/surefire-reports/*.xml'
                        jacoco(
                            execPattern: 'target/jacoco.exec',
                            classPattern: 'target/classes',
                            sourcePattern: 'src/main/java',
                            exclusionPattern: 'src/test*'
                        )
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                dir('backend') {
                    sh 'mvn package -DskipTests'
                }
            }
            post {
                success {
                    dir('backend') {
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    }
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                dir('backend') {
                    sh '''
                        SONAR_TOKEN=${SONAR_TOKEN} \
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                            -Dsonar.projectKey=aarch64-sms \
                            -Dsonar.organization=aarch64 \
                            -Dsonar.host.url=https://sonarcloud.io \
                            -Dsonar.qualitygate.wait=false
                    '''
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}