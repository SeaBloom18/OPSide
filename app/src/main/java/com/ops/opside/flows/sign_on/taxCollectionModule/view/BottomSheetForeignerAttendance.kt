package com.ops.opside.flows.sign_on.taxCollectionModule.view

import android.R
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.ops.opside.common.entities.firestore.ConcessionaireFE
import com.ops.opside.common.entities.firestore.OriginFE
import com.ops.opside.common.entities.share.ConcessionaireSE
import com.ops.opside.common.utils.ID
import com.ops.opside.common.utils.animateOnPress
import com.ops.opside.databinding.BottomSheetForeignerAttendanceBinding
import com.ops.opside.flows.sign_off.registrationModule.viewModel.RegisterViewModel
import com.ops.opside.flows.sign_on.taxCollectionModule.viewModel.BottomSheetForeignerAttendanceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetForeignerAttendance(
    private val registration: (ConcessionaireFE) -> Unit = {}
) : BottomSheetDialogFragment() {

    private val mBinding: BottomSheetForeignerAttendanceBinding by lazy {
        BottomSheetForeignerAttendanceBinding.inflate(layoutInflater)
    }

    private val mViewModel: BottomSheetForeignerAttendanceViewModel by viewModels()
    private val mRegistrationViewModel: RegisterViewModel by viewModels()

    private var mForeignerConcessionaire = ConcessionaireFE()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.apply {
            btnClose.animateOnPress()
            btnClose.setOnClickListener { dismiss() }

            tilEmail.setEndIconOnClickListener {
                val email = teEmail.text.toString()
                if (isValidEmail(email))
                    mViewModel.getEmailInformation(email)
                else
                    Toast.makeText(context, "Correo invalido", Toast.LENGTH_SHORT).show()
            }

            btnRegister.setOnClickListener { completeRegister() }


        }

        bindViewModel()
        mRegistrationViewModel.getOriginList()
    }

    private fun bindViewModel() {
        mViewModel.getEmailInformation.observe(this, Observer(this::getEmailInformation))

        mRegistrationViewModel.getOriginList.observe(this, Observer(this::getOriginList))
        mRegistrationViewModel.registerConcessionaire.observe(this, Observer(this::wasRegistered))
    }

    private fun completeRegister() {
        if (validateFields(
                mBinding.tilEmail,
                mBinding.tilName,
                mBinding.tilOrigin,
                mBinding.tilLinearMeters
            ) and
            isValidEmail(mBinding.teEmail.text.toString())
        ) {
            if (mForeignerConcessionaire.idFirebase.isNotEmpty()) {
                mForeignerConcessionaire.linearMeters =
                    mBinding.teLinearMeters.text.toString().toDouble()

                registration.invoke(mForeignerConcessionaire)

                dismiss()
            } else {
                mForeignerConcessionaire = ConcessionaireFE(
                    isForeigner = true,
                    email = mBinding.teEmail.text.toString(),
                    name = mBinding.teName.text.toString(),
                    origin = mBinding.teOrigin.text.toString(),
                    linearMeters = mBinding.teLinearMeters.text.toString().toDouble(),
                    idFirebase = ID.getTemporalId(),

                    address = "",
                    phone = "",
                    role = 1,
                    lineBusiness = "",
                    absence = 0,
                    password = "",
                    participatingMarkets = mutableListOf()
                )

                mRegistrationViewModel.insertConcessionaire(mForeignerConcessionaire)
            }
        }
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean {
        var isValid = true
        for (textField in textFields) {
            if (textField.editText?.text.toString().trim().isEmpty()) {
                textField.error = getString(com.ops.opside.R.string.login_til_required)
                isValid = false
            } else {
                textField.error = null
            }
        }
        return isValid
    }

    private fun wasRegistered(itWasRegistered: Boolean) {
        Toast.makeText(
            context,
            "Concesionario foraneo registrado",
            Toast.LENGTH_LONG
        ).show()

        registration.invoke(mForeignerConcessionaire)

        dismiss()
    }


    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun getOriginList(origins: MutableList<OriginFE>) {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireContext(), R.layout.simple_list_item_1,
                origins.map { it.originName }.toMutableList()
            )

        mBinding.teOrigin.setAdapter(adapter)
    }

    private fun getEmailInformation(concessionaire: ConcessionaireSE?) {
        if (concessionaire == null) {
            Toast.makeText(
                context,
                "El concesionario no existe\nLlena los demás campos solicitados",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (concessionaire.isForeigner.not()) {

            Toast.makeText(
                context,
                "El concesionario ya existe pero no es foraneo",
                Toast.LENGTH_LONG
            ).show()

            return
        }


        with(mBinding) {
            teName.setText(concessionaire.name)
            teOrigin.setText(concessionaire.origin)

            mForeignerConcessionaire = concessionaire.parseToFe()
        }
    }
}