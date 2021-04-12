package org.cqfn.save.frontend.components.views

import org.cqfn.save.entities.Project
import org.cqfn.save.frontend.components.tables.tableComponent
import org.cqfn.save.frontend.utils.get
import org.cqfn.save.frontend.utils.unsafeMap

import org.w3c.fetch.Headers
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.child
import react.dom.a
import react.dom.td
import react.table.columns

import kotlinx.browser.window
import kotlinx.coroutines.await

/**
 * A view with collection of projects
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class CollectionView : RComponent<RProps, RState>() {
    @Suppress("EMPTY_BLOCK_STRUCTURE_ERROR", "TOO_LONG_FUNCTION", "MAGIC_NUMBER")
    override fun RBuilder.render() {
        child(tableComponent(
            columns = columns {
                column(id = "index", header = "#") {
                    td {
                        +"${it.row.index}"
                    }
                }
                column(id = "name", header = "Name") {
                    td {
                        a(href = "#/${it.value.type}/${it.value.owner}/${it.value.name}") {
                            +it.value.name
                        }
                    }
                }
                column(id = "passed", header = "Tests passed") {
                    td {
                        a(href = "#/${it.value.type}/${it.value.owner}/${it.value.name}/history") {
                            +(it.value.description ?: "Description N/A")
                        }
                    }
                }
            },
            initialPageSize = 10,
            useServerPaging = false,
        ) {
            get(
                url = "${window.location.origin}/projects",
                headers = Headers().also {
                    it.set("Accept", "application/json")
                },
            )
                .unsafeMap {
                    it.json()
                        .await()
                        .unsafeCast<Array<Project>>()
                }
        }) { }
    }
}
