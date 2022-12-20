package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ops.opside.BuildConfig
import com.ops.opside.R
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetShowQrBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.BottomSheetShowQrViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


@AndroidEntryPoint
class BottomSheetShowQr: BaseBottomSheetFragment() {

    private lateinit var mBinding: BottomSheetShowQrBinding
    private val mViewModel: BottomSheetShowQrViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mBinding = BottomSheetShowQrBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {
            ivBack.setOnClickListener { dismiss() }
            ivPrintQR.setOnClickListener { generatePDFBadgeSize() }
        }
        generateQr()
    }

    /** Methods **/
    private fun generateQr(): Bitmap? {
        val mWriter = MultiFormatWriter()
        var mBitmap: Bitmap? = null
        return try {
            val mMatrix = mWriter.encode(
                mViewModel.getFirebaseId().orEmpty(),
                BarcodeFormat.QR_CODE,
                600,
                600
            )
            val mEncoder = BarcodeEncoder()
            mBitmap = mEncoder.createBitmap(mMatrix)
            mBinding.imgQr.setImageBitmap(mBitmap)
            createDocument(mBitmap)
            mBitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            mBitmap
        }
    }

    private fun createDocument(mBitmap: Bitmap) {
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "${R.string.bottom_sheet_showqr_image_name}"
        )
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
    }

    /** PDF TAMAÑO CARTA **/
    private fun generatePDFLetterSize() {
        /** Explicacion de generacion de PDF **/
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val textUseConditions = TextPaint()
        val textOpsDescription = TextPaint()
        val useConditionTextLine1 = "${R.string.bottom_sheet_showqr_condition1} \n"
        val useConditionTextLine2 = "${R.string.bottom_sheet_showqr_condition2}"
        val useConditionTextLine3 = "${R.string.bottom_sheet_showqr_condition3} \n"
        val opsDescriptionTextLine1 = "${R.string.bottom_sheet_showqr_description1}"
        val opsDescriptionTextLine2 = "${R.string.bottom_sheet_showqr_description2}"

        val paginaInfo = PdfDocument.PageInfo.Builder(200, 270, 1).create()
        val pagina1 = pdfDocument.startPage(paginaInfo)

        //Nota: para pintar un recurso (imagen)
        val canvas = pagina1.canvas

        /*val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ixtlahuacan_logo)
        val bitmapEscala = Bitmap.createScaledBitmap(bitmap, 60,40, false)
        canvas.drawBitmap(bitmapEscala, 20f, 20f, paint)*/

        val bitmapEscala1 = generateQr()?.let { Bitmap.createScaledBitmap(it, 120, 120, false) }
        bitmapEscala1?.let { canvas.drawBitmap(it, 40f, 70f, paint) }

        //Print textLines, basic configurations
        textUseConditions.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        textUseConditions.textSize = 6f
        canvas.drawText(useConditionTextLine1, 20f, 200f, textUseConditions)
        canvas.drawText(useConditionTextLine2, 20f, 210f, textUseConditions)
        canvas.drawText(useConditionTextLine3, 20f, 220f, textUseConditions)

        textOpsDescription.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        textOpsDescription.textSize = 4f
        canvas.drawText(opsDescriptionTextLine1, 130f, 265f, textUseConditions)
        canvas.drawText(opsDescriptionTextLine2, 115f, 20f, textUseConditions)


        pdfDocument.finishPage(pagina1)

        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            getString(R.string.bottom_sheet_showqr_pdf_name)
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            toast(getString(R.string.bottom_sheet_showqr_toast_create_success))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }

    /** PDF TAMAÑO GAFETE **/
    private fun generatePDFBadgeSize() {
        /** Explicacion de generacion de PDF **/
        val pdfDocument = PdfDocument()
        val paint = Paint()

        var x = 0f
        var y = 0f
        val text = TextPaint()

        var canvas = Canvas()
        var pagina: PdfDocument.Page? = null
        for (i in 1..46){
            if (y == 0f && x == 0f){
                val paginaInfo = PdfDocument.PageInfo.Builder(200, 270, 1).create()
                pagina = pdfDocument.startPage(paginaInfo)
                //Nota: para pintar un recurso (imagen)
                canvas = pagina.canvas
            }

            val bitmapEscala1 = generateQr()?.let { Bitmap.createScaledBitmap(it, 50, 50, false) }
            bitmapEscala1?.let { canvas.drawBitmap(it, x, y, paint) }


            //Print textLines, basic configurations
            text.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            text.textSize = 7f
            canvas.drawText("OPS", x + 65, y + 25, text)
            text.textSize = 4f
            canvas.drawText("Mario Razo Valenzuela", x + 50, y + 32, text)

            x += 100f

            if (x > 150f){
                x = 0f
                y += 50f
            }


            if (y > 200f || i == 46) {
                y = 0f
                pdfDocument.finishPage(pagina)
            }
        }

        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            getString(R.string.bottom_sheet_showqr_pdf_name)
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            val target = Intent(Intent.ACTION_VIEW)

            val uri = FileProvider.getUriForFile(
                Objects.requireNonNull(requireContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);

            target.setDataAndType(uri, "application/pdf")
            target.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
            val intent = Intent.createChooser(target, "Open File")
            startActivity(intent)
            pdfDocument.close()
        } catch (e: Exception) {
            Log.d("Demo", e.message.toString())
        }

    }
}