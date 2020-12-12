def call(ObjectModel model) {

    docker.image(model.image).inside {

        model.steps.each { step ->

            if (step.isParallelStep()) {
                parallel {
                    step.parallel.each { pStep ->
                        stage(pStep.name) {
                            pStep.actions.each { action ->
                                sh action
                            }
                        }
                    }
                }
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
