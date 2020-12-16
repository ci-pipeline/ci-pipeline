def call(ObjectModel model) {

    def env = ""
    model.variables.each {
        env += "-e $it "
    }

    def image2Container = [:]

    model.services.each { image ->
        container = docker.image(image).run(env)
        image2Container[image] = container
    }

    return image2Container
}
