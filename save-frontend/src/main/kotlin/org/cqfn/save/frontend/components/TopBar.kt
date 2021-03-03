package org.cqfn.save.frontend.components

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import react.dom.div
import react.dom.i
import react.dom.img
import react.dom.li
import react.dom.nav
import react.dom.ol
import react.dom.span
import react.dom.ul
import react.router.dom.RouteResultProps

import kotlinx.html.id
import kotlinx.html.role

/**
 * A component for web page top bar
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class TopBar : RComponent<RouteResultProps<RProps>, RState>() {
    @Suppress("TOO_LONG_FUNCTION", "EMPTY_BLOCK_STRUCTURE_ERROR")
    override fun RBuilder.render() {
        nav("navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow") {
            // Topbar Navbar
            nav("navbar-nav mr-auto") {
                attrs["aria-label"] = "breadcrumb"
                ol("breadcrumb") {
                    li("breadcrumb-item") {
                        attrs["aria-current"] = "page"
                        a(href = "#/") {
                            +"SAVE"
                        }
                    }
                    props.location.pathname
                        .split("/")
                        .filterNot { it.isBlank() }
                        .apply {
                            foldIndexed("#") { index: Int, acc: String, pathPart: String ->
                                val currentLink = "$acc/$pathPart"
                                li("breadcrumb-item") {
                                    attrs["aria-current"] = "page"
                                    if (index == size - 1) {
                                        attrs["active"] = "true"
                                    }
                                    a(href = currentLink) {
                                        +pathPart
                                    }
                                }
                                currentLink
                            }
                        }
                }
            }
            ul("navbar-nav ml-auto") {
                div("topbar-divider d-none d-sm-block") {}
                // Nav Item - User Information
                li("nav-item dropdown no-arrow") {
                    a("#", classes = "nav-link dropdown-toggle") {
                        attrs {
                            id = "userDropdown"
                            role = "button"
                            set("data-toggle", "dropdown")
                            set("aria-haspopup", "true")
                            set("aria-expanded", "false")
                        }
                        span("mr-2 d-none d-lg-inline text-gray-600 small") {
                            +"User Name"  // todo receive username from App, store it in State and re-render on updates
                        }
                        img(classes = "img-profile rounded-circle", src = "img/undraw_profile.svg") {}
                    }
                    // Dropdown - User Information
                    div("dropdown-menu dropdown-menu-right shadow animated--grow-in") {
                        attrs["aria-labelledby"] = "userDropdown"
                        a("#", classes = "dropdown-item") {
                            i("fas fa-user fa-sm fa-fw mr-2 text-gray-400") {
                                +"Profile"
                            }
                        }
                        a("#", classes = "dropdown-item") {
                            i("fas fa-cogs fa-sm fa-fw mr-2 text-gray-400") {
                                +"Settings"
                            }
                        }
                        a("#", classes = "dropdown-item") {
                            i("fas fa-sign-out fa-sm fa-fw mr-2 text-gray-400") {
                                +"Log out"
                            }
                        }
                    }
                }
            }
        }
    }
}
