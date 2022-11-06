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
import com.ops.opside.databinding.BottomSheetShowQrBinding
import com.ops.opside.flows.sign_on.dealerModule.view.viewModel.BottomSheetShowQrViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.lang.Exception


@AndroidEntryPoint
class BottomSheetShowQr: BottomSheetDialogFragment() {

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
            btnPrintQRCode.setOnClickListener { generatePDF() }
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
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "myQrCode.png")
        val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        os.close()
    }

    private fun generatePDF() {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val titulo = TextPaint()
        val descripcion = TextPaint()
        val tituloText = "NOTA: Agregar descripcion de uso"
        val opsText = "Propiedad de OPSide"

        val paginaInfo = PdfDocument.PageInfo.Builder(215, 270, 1).create()
        val pagina1 = pdfDocument.startPage(paginaInfo)

        val canvas = pagina1.canvas

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ixtlahuacan_logo)
        val bitmapEscala = Bitmap.createScaledBitmap(bitmap, 60,40, false)
        val bitmapEscala1 = generateQr()?.let { Bitmap.createScaledBitmap(it, 80,80, false) }
        canvas.drawBitmap(bitmapEscala, 20f, 20f, paint)
        bitmapEscala1?.let { canvas.drawBitmap(it, 70f, 100f, paint) }

        titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        titulo.textSize = 8f
        canvas.drawText(tituloText, 50f, 200f, titulo)

        descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        descripcion.textSize = 4f
        canvas.drawText(opsText, 90f, 250f, titulo)


        pdfDocument.finishPage(pagina1)

        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MyQrCode.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(requireContext(), "Se creo el PDF correctamente", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        pdfDocument.close()

    }
}