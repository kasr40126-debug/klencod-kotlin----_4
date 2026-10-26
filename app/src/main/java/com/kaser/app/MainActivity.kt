
package com.kaser.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(10, 25, 39)
    private val panel = Color.rgb(22, 43, 58)
    private val gold = Color.rgb(230, 194, 112)
    private val white = Color.WHITE

    private lateinit var list: LinearLayout
    private lateinit var chatArea: LinearLayout
    private lateinit var input: EditText
    private lateinit var scroll: ScrollView
    private var currentChat = "محادثة جديدة"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun label(
        value: String,
        size: Float,
        color: Int,
        bold: Boolean = false
    ) = TextView(this).apply {
        text = value
        textSize = size
        setTextColor(color)
        gravity = Gravity.CENTER_VERTICAL
        setPadding(16, 12, 16, 12)
        if (bold) setTypeface(null, Typeface.BOLD)
    }

    private fun root() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(bg)
        setPadding(8, 8, 8, 8)
    }

    private fun button(value: String, action: () -> Unit) =
        Button(this).apply {
            text = value
            setTextColor(bg)
            setBackgroundColor(gold)
            setOnClickListener { action() }
        }

    private fun showHome() {
        val page = root()

        page.addView(label("◆ KASER APP", 28f, gold, true))
        page.addView(label("المراسلة الدولية", 18f, white))
        page.addView(label("محادثاتك في مكان واحد", 14f, gold))

        val search = EditText(this).apply {
            hint = "ابحث عن محادثة"
            setTextColor(white)
            setHintTextColor(Color.LTGRAY)
            setSingleLine()
            setPadding(16, 12, 16, 12)
            setBackgroundColor(panel)
        }
        page.addView(search)

        page.addView(label("المحادثات", 22f, gold, true))

        list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scrollList = ScrollView(this)
        scrollList.addView(list)
        page.addView(
            scrollList,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        button("+ بدء محادثة", { newChat() }.let { it })
            .also { page.addView(it) }

        setContentView(page)
    }

    private fun newChat() {
        val field = EditText(this).apply {
            hint = "رقم الهاتف الدولي، مثال +967..."
            setTextColor(white)
            setHintTextColor(Color.GRAY)
        }

        AlertDialogBuilder(field)
    }

    private fun AlertDialogBuilder(field: EditText) {
        android.app.AlertDialog.Builder(this)
            .setTitle("محادثة جديدة")
            .setMessage("أدخل رقم هاتف المستخدم")
            .setView(field)
            .setPositiveButton("فتح") { _, _ ->
                val number = field.text.toString().trim()
                if (number.isNotEmpty()) {
                    currentChat = number
                    showChat()
                }
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    private fun showChat() {
        val page = root()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(panel)
        }

        header.addView(button("رجوع") { showHome() })
        header.addView(
            label(currentChat, 17f, gold, true),
            LinearLayout.LayoutParams(0, -2, 1f)
        )
        page.addView(header)

        scroll = ScrollView(this)
        chatArea = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 12, 8, 12)
        }
        scroll.addView(chatArea)
        page.addView(
            scroll,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        val composer = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(panel)
        }

        input = EditText(this).apply {
            hint = "اكتب رسالتك الطويلة..."
            setTextColor(white)
            setHintTextColor(Color.LTGRAY)
            minLines = 1
            maxLines = 5
            setPadding(12, 8, 12, 8)
            setBackgroundColor(bg)
        }

        composer.addView(
            input,
            LinearLayout.LayoutParams(0, -2, 1f)
        )
        composer.addView(button("إرسال") { sendMessage() })
        page.addView(composer)

        setContentView(page)
        addBubble("أنت الآن في محادثة $currentChat")
    }

    private fun sendMessage() {
        val message = input.text.toString().trim()
        if (message.isEmpty()) return

        addBubble(message)
        input.text.clear()
    }

    private fun addBubble(message: String) {
        val bubble = TextView(this).apply {
            text = message
            textSize = 17f
            setTextColor(white)
            setPadding(16, 12, 16, 12)
            setBackgroundColor(panel)
        }

        chatArea.addView(
            bubble,
            LinearLayout.LayoutParams(-1, -2).apply {
                setMargins(4, 6, 4, 6)
            }
        )
        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }
}
