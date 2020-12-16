def call(ObjectModel model) {
    timeout(time: 10, unit: 'MINUTES') {
        withEnv(model.variables) {
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'XTerm']) {

                def image2Container = [:]
                try {
                    image2Container = execService(model)
                    execSteps(model, image2Container)
                } finally {
                    image2Container.each { image, container ->
                        println("cleaning for image=${image}, container=${container} ...")
                        container.stop()
                        deleteImage(image)
                    }
                }
            }
        }
    }
}
