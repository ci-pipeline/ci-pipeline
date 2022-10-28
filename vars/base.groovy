def call(ObjectModel model) {

    timeout(time: model.timeout, unit: 'MINUTES') {
        withEnv(model.variables) {
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {

                def network = dockerNetwork(model)
                def image2Container = null

                try {
                    image2Container = execService(model, network)
                    execSteps(model, network)
                } finally {
                    image2Container?.each { image, container ->
                        println("cleaning for image=${image}, container=${container} ...")
                        container.stop()
                        dockerRmi(image)
                    }
                    network.remove()
                }
            }
        }
    }
}
