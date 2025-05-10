package objects.tasks

import io.github.oshai.kotlinlogging.KotlinLogging

class Tasks {
    var tasks: MutableList<Task> = mutableListOf()
    private val logger = KotlinLogging.logger {}

    fun add(task: Task) {
        logger.info { "Adding task: $task" }
        tasks.add(task)
    }

    fun remove(task: Task) {
        tasks.remove(task)
    }

    fun get(index: Int): Task {
        return tasks[index]
    }

}