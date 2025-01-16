package taskManager.core.modles

import javax.persistence.*

@Entity
@Table(name = "files")
data class File(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0,

    @Column(name = "file_name")
    var fileName: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val fileUser: User
) {
    constructor() : this(0, "", User())
}