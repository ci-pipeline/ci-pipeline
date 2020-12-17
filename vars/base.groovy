def call(ObjectModel model) {

    timeout(time: 10, unit: 'MINUTES') {
        withEnv(model.variables) {
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {

                Network network = docker_network(model)
                def image2Container = null

                try {
                    image2Container = execService(model, network)
                    execSteps(model, network)
                } finally {
                    image2Container?.each { image, container ->
                        println("cleaning for image=${image}, container=${container} ...")
                        container.stop()
                        docker_rmi(image)
                    }
                    network.remove()
                }
            }
        }
    }
}
