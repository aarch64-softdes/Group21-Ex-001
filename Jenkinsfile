pipeline {
    agent any
    
    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }
    
    environment {
        JAVA_HOME = tool 'JDK21'
        MAVEN_HOME = tool 'Maven3'
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
                    sh 'mvn sonar:sonar -Dsonar.projectKey=student-management-system -Dsonar.projectName="Student Management System"'
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