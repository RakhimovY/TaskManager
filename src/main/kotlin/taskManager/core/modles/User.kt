package taskManager.core.modles

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "email")
    var email: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "phone_number")
    var phoneNumber: String,

    @Column(name = "registrationDate")
    var registrationDate: String? = LocalDate.now().toString(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val tasks: List<Task> = listOf(),

    @OneToOne(
        mappedBy = "fileUser",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    val file: File? = null
) {
    constructor() : this(0, "", "", "", "")
}