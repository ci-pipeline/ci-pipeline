def call(ObjectModel model, def network) {

    def env = ""
    model.variables.each {
        env += "-e $it "
    }

    def image2Container = [:]

    model.services.each { image ->
        container = docker.image(image)
                .run(env, network.getCliArgs(), "--net-alias=${image.split(":")[0]}")
        image2Container[image] = container
    }

    return image2Container
}
