pipeline {
    agent any
    
    tools {
        jdk 'JDK 21'
        maven 'Maven 3.9.6'
    }

    environment {
        SONAR_TOKEN = credentials('sonarcloud-token')
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
                    archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Code Quality') {
            steps {
                dir('backend') {
                    sh '''
                    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
                        -Dsonar.projectKey=aarch64-softdes_Group21-Ex-001
                        -Dsonar.organization=aarch64-softdes
                        -Dsonar.host.url=https://sonarcloud.io
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