package com.chsteam.mypets.internal.database

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val from: Int,
    val text: String,
    val picture: List<String>,
    val date: Date
) :  KoinComponent {

    private val npcDao: NpcDao by inject()

    var npc: MutableStateFlow<Npc?> = MutableStateFlow(null)
    var imageBitmaps: MutableStateFlow<List<ImageBitmap>?> = MutableStateFlow(null)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getNPC()?.let{
                npc.emit(it)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            imageBitmaps.emit(getPostImageBitmap())
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun getPostImageBitmap(): List<ImageBitmap> = withContext(Dispatchers.IO) {
        try {
            picture.mapNotNull { base64String ->
                val bytes = Base64.decode(base64String)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.asImageBitmap()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getNPC() : Npc? {
        return npcDao.getNpcById(this.from)
    }

    @Composable
    fun PostCard() {
        val npcData by npc.collectAsState()
        val images by imageBitmaps.collectAsState()
        if (npcData != null && images != null) {
            ElevatedCard {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = npcData!!.imageBitmap.value,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = npcData!!.name)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn {
                            item {
                                images!!.forEach {
                                    Image(
                                        bitmap = it,
                                        contentDescription = "Post Picture"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Query("SELECT * FROM Post WHERE id = :id")
    suspend fun getPostById(id: Int): Post?

    @Query("SELECT * FROM Post")
    suspend fun getAllPosts(): List<Post>

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)
}