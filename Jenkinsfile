pipeline {
    agent any
    options {
    timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }

    environment {

        TIME_ZONE = 'Asia/Seoul'

        //github
        TARGET_BRANCH = 'release'
        REPOSITORY_URL= 'https://github.com/SWM-304/TeamPlanner-BE'

        //docker-hub
        registryCredential = 'docker-hub'

        //aws ecr

        CONTAINER_NAME = 'teamplanner-backend-container'
        AWS_CREDENTIAL_NAME = 'AWS_ECR'
        ECR_PATH = '129715120090.dkr.ecr.ap-northeast-2.amazonaws.com'
        IMAGE_NAME = '129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver'
        REGION = 'ap-northeast-2'
    }



    stages {


        stage('init') {
            steps {
                echo 'init stage'
                deleteDir()
            }
            post {
                success {
                    echo 'success init in pipeline'
                }
                failure {
                    error 'fail init in pipeline'
                }
            }
        }

        stage('Prepare') {
            steps {
                echo 'Cloning Repository'
                git branch: 'release',
                    credentialsId: 'repo-and-hook-access-token-credentials',
                    url: 'https://github.com/SWM-304/TeamPlanner-BE'
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
       // 일단은 테스트없이 빌드
        stage('Build Gradle') {
            steps {
                echo 'Build Gradle'

                dir('.'){
                    sh '''
                        pwd
                        cd /var/jenkins_home/workspace/teamPlannerBackEnd_jenkinsFile
                        chmod +x ./gradlew
                        ./gradlew build --exclude-task test
                    '''
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
       // 도커 이미지를 만든다. build number로 태그를 주되 latest 태그도 부여한다.
        stage('Build Docker') {
            steps {
                echo 'Build Docker'
                sh """
                    cd /var/jenkins_home/workspace/teamPlannerBackEnd_jenkinsFile
                    docker builder prune
                    docker build -t $IMAGE_NAME:$BUILD_NUMBER .
                    docker tag $IMAGE_NAME:$BUILD_NUMBER $IMAGE_NAME:latest
                """
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
       // 빌드넘버 태그와 latest 태그 둘 다 올린다.
        stage('Push Docker') {
            steps {
                echo 'Push Docker'
                script {
                    // cleanup current user docker credentials
                    sh 'rm -f ~/.dockercfg ~/.docker/config.json || true'

                    docker.withRegistry("https://${ECR_PATH}", "ecr:${REGION}:${AWS_CREDENTIAL_NAME}") {
                        docker.image("${IMAGE_NAME}:${BUILD_NUMBER}").push()
                        docker.image("${IMAGE_NAME}:latest").push()
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
       // aws cli 로그인 ->  및 ecr pull -> origin container delete -> docker compose down -> docker compose up -> origin image delete
        stage('Docker Run') {
            steps {
                echo 'Pull Docker Image & Docker Image Run'
                sshagent(credentials: ['ssh']) {



                    script {

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'sudo chmod 666 /var/run/docker.sock'"

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com'"

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'docker compose down'"
                        // Delete existing Docker image
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'sudo docker rmi -f 129715120090.dkr.ecr.ap-northeast-2.amazonaws.com/teamplanner-backendserver:latest'"

                        docker.withRegistry("https://${ECR_PATH}", "ecr:${REGION}:${AWS_CREDENTIAL_NAME}") {

                        docker.image("${IMAGE_NAME}:${BUILD_NUMBER}").pull()

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'sudo docker ps -q --filter name=${CONTAINER_NAME}'"

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'containers=\$(sudo docker ps -aq --filter name=${CONTAINER_NAME}); if [ -n \"\$containers\" ]; then sudo docker rm -f \$containers; fi'"

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'docker compose up -d'"

                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.1.3.222 'images=\$(sudo docker images -q -f dangling=true); if [ -n \"\$images\" ]; then sudo docker rmi -f \$images; fi'"

                        }
                    }


                }
            }
        }
    stage('Clean Up Docker Images on Jenkins Server') {
        steps {
            echo 'Cleaning up unused Docker images on Jenkins server'

            // Clean up unused Docker images, including those created within the last hour
            sh "docker image prune -f --all --filter \"until=1h\""
        }
    }



}

    post {
        success {
            slackSend (channel: '#cicd-notification', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend (channel: '#cicd-notification', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}
