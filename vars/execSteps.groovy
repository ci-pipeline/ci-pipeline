def call(ObjectModel model) {

    docker.image(model.image).inside {

        model.steps.each { step ->

            if (step.isParallelStep()) {

                def stages = [:]

                step.parallel.each { pStep ->
                    stages[pStep.name] = { buildStep(pStep) }
                }

                parallel(stages)

            } else {
                stage(step.name) {
                    buildStep(step)
                }
            }
        }
    }
}

private def buildStep(def step) {
    step.only.each {
        when { branch it }
    }
    step.except.each {
        when { not { branch it } }
    }

    step.actions.each { action ->
        sh action
    }
}
