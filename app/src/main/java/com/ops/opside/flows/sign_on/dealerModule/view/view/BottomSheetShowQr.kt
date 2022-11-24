package com.ops.opside.flows.sign_on.dealerModule.view.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ops.opside.R
import com.ops.opside.common.views.BaseBottomSheetFragment
import com.ops.opside.databinding.BottomSheetShowQrBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.BottomSheetShowQrViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.lang.Exception


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
            ivPrintQR.setOnClickListener { generatePDFLetterSize() }
        }
        generateQr()
    }

    /** Methods **/
    private fun generateQr(): Bitmap? {
        val mWriter = MultiFormatWriter()
        var mBitmap: Bitmap? = null
        return try {
            val mMatrix = mWriter.encode(mViewModel.getFirebaseId().orEmpty(), BarcodeFormat.QR_CODE, 600, 600)
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
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "myImageQrCode.png")
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
        val useConditionTextLine1 = "Este tipo de documentos es exclusivo para usuarios de OPS. \n"
        val useConditionTextLine2 = "Favor de hacer uso exclusivo en recaudacion."
        val useConditionTextLine3 = "Cualquier duda contactar con administracion. \n"
        val opsDescriptionTextLine1 = "Propiedad de OPSide®"
        val opsDescriptionTextLine2 = "Ixtlahuacan de los Membrillos"

        val paginaInfo = PdfDocument.PageInfo.Builder(200, 270, 1).create()
        val pagina1 = pdfDocument.startPage(paginaInfo)

        //Nota: para pintar un recurso (imagen)
        val canvas = pagina1.canvas

        /*val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ixtlahuacan_logo)
        val bitmapEscala = Bitmap.createScaledBitmap(bitmap, 60,40, false)
        canvas.drawBitmap(bitmapEscala, 20f, 20f, paint)*/

        val bitmapEscala1 = generateQr()?.let { Bitmap.createScaledBitmap(it, 120,120, false) }
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
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MyLetterQrCode.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "Se creo el PDF correctamente", Toast.LENGTH_LONG).show()
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
        val textUseConditions = TextPaint()
        val textOpsDescription = TextPaint()
        val useConditionTextLine1 = "Este tipo de documentos es exclusivo para usuarios de OPS. \n"
        val useConditionTextLine2 = "Favor de hacer uso exclusivo en recaudacion."
        val useConditionTextLine3 = "Cualquier duda contactar con administracion. \n"
        val opsDescriptionTextLine1 = "Propiedad de OPSide®"
        val opsDescriptionTextLine2 = "Ixtlahuacan de los Membrillos"

        val pageInfo = PdfDocument.PageInfo.Builder(76, 102, 1).create()
        val page1 = pdfDocument.startPage(pageInfo)

        //Nota: para pintar un recurso (imagen) es necesaria la siguiente configuracion
        val canvas = page1.canvas

        /*val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ixtlahuacan_logo_v2)
        val bitmapImg1 = Bitmap.createScaledBitmap(bitmap, 50,50, false)
        canvas.drawBitmap(bitmapImg1, 0f, 0f, paint)*/

        //Img size
        val bitmapImg2 = generateQr()?.let { Bitmap.createScaledBitmap(it, 50,50, false) }
        //Img location
        bitmapImg2?.let { canvas.drawBitmap(it, 12f, 16f, paint) }

        //Print textLines, basic configurations
        textUseConditions.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        textUseConditions.textSize = 2.5f
        canvas.drawText(useConditionTextLine1, 8f, 68f, textUseConditions)
        canvas.drawText(useConditionTextLine2, 8f, 71f, textUseConditions)
        canvas.drawText(useConditionTextLine3, 8f, 74f, textUseConditions)

        textOpsDescription.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        textOpsDescription.textSize = 3f
        canvas.drawText(opsDescriptionTextLine1, 40f, 100f, textOpsDescription)
        canvas.drawText(opsDescriptionTextLine2, 12f, 15f, textOpsDescription)


        pdfDocument.finishPage(page1)

        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MyBadgeQrCode.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "Se creo el PDF correctamente", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        pdfDocument.close()

    }
}