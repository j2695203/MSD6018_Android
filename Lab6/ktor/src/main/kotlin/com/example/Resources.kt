package com.example

import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.routing.routing


import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureResources(){
    install(Resources)
    routing {
        get<Posts>{
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) { 
                    Post.selectAll()
                        .map {
                            PostData(
                                it[Post.id].value,
                                it[Post.content],
                                it[Post.postTime]
                            )
                        }

                }
            )
        }

        get<Posts.Time>{
            val time = call.receive<PostTime>().postTime
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post.select(Post.postTime greaterEq  time)
                        .map {
                            PostData(
                                it[Post.id].value,
                                it[Post.content],
                                it[Post.postTime]
                            )
                        }

                }
            )
        }

        get<Posts.ID>{
            val id = call.receive<PostID>().id
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post.select(Post.id eq id)
                        .map {
                            PostData(
                                it[Post.id].value,
                                it[Post.content],
                                it[Post.postTime]
                            )
                        }

                }
            )
        }

        post<Posts.Content> {
            val postContent = call.receive<PostContent>().content
            val time = System.currentTimeMillis()
            newSuspendedTransaction(Dispatchers.IO, DBSettings.db) {
                Post.insertAndGetId {
                    it[content] = postContent
                    it[postTime]  = time
                }
                call.respondText { "New Post : $postContent" }
            }
        }


        delete<Posts.Delete>{
            val id = call.receive<PostID>().id
            newSuspendedTransaction(Dispatchers.IO, DBSettings.db) {
                Post.deleteWhere { Post.id eq id }
            }
            call.respondText("Post $id has been deleted")
        }
    }

}

@Serializable data class PostData(val id: Int, val content: String, val postTime: Long)
@Serializable data class PostID(val id: Int)
@Serializable data class PostContent(val content: String)
@Serializable data class PostTime(val postTime: Long)

@Resource("/posts") //corresponds to /posts
class Posts {

    @Resource("/time") //corresponds to /posts/time
    class Time(val parent : Posts = Posts())

    @Resource("/id") //corresponds to /posts/id
    class ID(val parent : Posts = Posts())

    @Resource("/content")
    class Content(val parent : Posts = Posts(), val content : String? = "")

    @Resource("/delete")
    class Delete(val parent : Posts = Posts())


}