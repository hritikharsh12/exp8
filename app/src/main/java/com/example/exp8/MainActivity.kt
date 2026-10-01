package com.example.exp8

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exp8.ui.theme.Exp8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exp8Theme {
                WebViewMenuApp()
            }
        }
    }
}

data class Bookmark(val name: String, val url: String)

val presetBookmarks = listOf(
    Bookmark("Google", "https://www.google.com"),
    Bookmark("Wikipedia", "https://www.wikipedia.org"),
    Bookmark("GitHub", "https://www.github.com"),
    Bookmark("Android Dev", "https://developer.android.com"),
    Bookmark("Stack Overflow", "https://stackoverflow.com")
)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewMenuApp() {
    var currentUrl by remember { mutableStateOf("https://www.google.com") }
    var inputUrl by remember { mutableStateOf("https://www.google.com") }
    var pageTitle by remember { mutableStateOf("Web Browser") }
    var isLoading by remember { mutableStateOf(false) }
    var progress by remember { mutableIntStateOf(0) }
    var webView: WebView? by remember { mutableStateOf(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    // Back handling for WebView navigation
    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    fun loadFormattedUrl(url: String) {
        val formatted = when {
            url.startsWith("http://") || url.startsWith("https://") -> url
            url.contains(".") -> "https://$url"
            else -> "https://www.google.com/search?q=$url"
        }
        currentUrl = formatted
        inputUrl = formatted
        webView?.loadUrl(formatted)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = pageTitle,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                        Text(
                            text = currentUrl,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { webView?.goBack() },
                        enabled = canGoBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Refresh Button
                    IconButton(onClick = { webView?.reload() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }

                    // Home Button
                    IconButton(onClick = { loadFormattedUrl("https://www.google.com") }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }

                    // Options Menu Dropdown
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu Options"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        Text(
                            text = "Bookmarks & Options",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.primary
                        )

                        presetBookmarks.forEach { bookmark ->
                            DropdownMenuItem(
                                text = { Text(bookmark.name) },
                                onClick = {
                                    showMenu = false
                                    loadFormattedUrl(bookmark.url)
                                }
                            )
                        }

                        DropdownMenuItem(
                            text = { Text("Clear Cache & History") },
                            onClick = {
                                showMenu = false
                                webView?.clearCache(true)
                                webView?.clearHistory()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("About Application") },
                            onClick = {
                                showMenu = false
                                showAboutDialog = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // URL Input Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputUrl,
                    onValueChange = { inputUrl = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter URL or search...") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    },
                    trailingIcon = {
                        if (inputUrl.isNotEmpty()) {
                            IconButton(onClick = { inputUrl = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear input"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = {
                        keyboardController?.hide()
                        loadFormattedUrl(inputUrl)
                    })
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = {
                        keyboardController?.hide()
                        loadFormattedUrl(inputUrl)
                    }
                ) {
                    Text("Go")
                }
            }

            // Quick Bookmark Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                presetBookmarks.forEach { bookmark ->
                    AssistChip(
                        onClick = { loadFormattedUrl(bookmark.url) },
                        label = { Text(bookmark.name) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            // Loading Progress Bar
            if (isLoading) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // WebView Container
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            builtInZoomControls = true
                            displayZoomControls = false
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                url?.let {
                                    currentUrl = it
                                    inputUrl = it
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                canGoBack = view?.canGoBack() == true
                                view?.title?.let { title ->
                                    if (title.isNotEmpty()) {
                                        pageTitle = title
                                    }
                                }
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                return false
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                progress = newProgress
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let {
                                    if (it.isNotEmpty()) {
                                        pageTitle = it
                                    }
                                }
                            }
                        }

                        loadUrl(currentUrl)
                        webView = this
                    }
                },
                update = { view ->
                    webView = view
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About Application") },
            text = {
                Text(
                    "This Android application demonstrates Menus and WebView implementation using Jetpack Compose and Material 3.\n\n" +
                            "Features:\n" +
                            "• Dropdown Options Menu\n" +
                            "• Navigation & Quick Bookmarks\n" +
                            "• Custom URL Loader with Search\n" +
                            "• Embedded WebView with progress indicator\n" +
                            "• System Back Button integration"
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
