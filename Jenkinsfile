node {
  stage('SC checkout'){
    git 'https://github.com/LabBhattcharjee/coreJavaPublic/tree/main/weather-service'
  } 
  
  stage('build install'){
    sh 'mvn clean install -DskipTests'
  }
}
