def call(Map args) {

    sh 'git config user.email "jenkins@bot.com"'
    sh 'git config user.name "Jenkins Bot"'

    // 파일 수정
    sh args.updateCommand

    withCredentials([
        usernamePassword(
            credentialsId: args.credentialsId,
            usernameVariable: 'USER',
            passwordVariable: 'PASS'
        )
    ]) {
        sh """
            git add ${args.file}
            git commit -m "${args.commitMessage}"
            git push https://\${USER}:\${PASS}@github.com/lyh4215/jenkins-study.git HEAD:${args.branch}
        """
    }
}
