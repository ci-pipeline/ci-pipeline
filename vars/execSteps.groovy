def call(ObjectModel model) {

    docker.image(model.image).inside {

        model.steps.each { step ->

            if (step.isParallelStep()) {

                def stages = [:]

                step.parallel.each { pStep ->
                    stages[pStep.name] = {
                        pStep.actions.each { action ->
                            sh action
                        }
                    }
                }

                parallel(stages)

            } else {
                stage(step.name) {
                    step.actions.each { action ->
                        sh action
                    }
                }
            }
        }
    }
}
