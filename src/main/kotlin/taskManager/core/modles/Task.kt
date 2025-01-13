package taskManager.core.modles

import taskManager.core.enum.TaskStatus
import javax.persistence.*

@Entity
@Table(name = "tasks")
data class Task (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "title")
    val title: String,

    @Column(name = "description")
    val description: String,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: TaskStatus = TaskStatus.NEW
){
    constructor() : this(0, "", "", TaskStatus.NEW) {
    }
}

