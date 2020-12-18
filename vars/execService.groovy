def call(ObjectModel model, def network) {

    def env = ""
    model.variables.each {
        env += "-e $it "
    }

    def image2Container = [:]

    model.services.each { image ->
        if (ObjectModel.isDockerService(image)) { // ignore docker here so to deal with it in execSteps
            return
        }

        args = "${env} ${network.getCliArgs()} --net-alias=${image.split(":")[0]}"

        container = docker.image(image).run(args)

        image2Container[image] = container
    }

    return image2Container
}
