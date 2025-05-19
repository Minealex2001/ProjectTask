package objects.tasks

class Tasks {
    var tasks: MutableList<Task> = mutableListOf()

    fun add(task: Task) {
        tasks.add(task)
    }

    fun remove(task: Task) {
        tasks.remove(task)
    }

    fun get(index: Int): Task {
        return tasks[index]
    }

}