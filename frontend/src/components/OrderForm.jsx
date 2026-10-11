import { useState } from 'react'

function OrderForm({ onSubmit }) {
  const [product, setProduct] = useState('')
  const [quantity, setQuantity] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!product.trim() || !quantity || quantity < 1) return
    
    setSubmitting(true)
    await onSubmit({ product: product.trim(), quantity: parseInt(quantity) })
    setSubmitting(false)
    setProduct('')
    setQuantity('')
  }

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
      <div className="flex items-center gap-3 mb-6">
        <div className="w-10 h-10 bg-indigo-100 rounded-xl flex items-center justify-center">
          <svg className="w-5 h-5 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
        </div>
        <h2 className="text-lg font-semibold text-slate-900">New Order</h2>
      </div>

      <form onSubmit={handleSubmit} className="space-y-5">
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-2">
            Product Name
          </label>
          <input
            type="text"
            value={product}
            onChange={(e) => setProduct(e.target.value)}
            placeholder="e.g., Laptop, Phone, Monitor"
            required
            className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl
                     focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent
                     text-slate-900 placeholder-slate-400 transition-all"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-slate-700 mb-2">
            Quantity
          </label>
          <input
            type="number"
            min="1"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            placeholder="1"
            required
            className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl
                     focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-transparent
                     text-slate-900 placeholder-slate-400 transition-all"
          />
        </div>

        <button
          type="submit"
          disabled={submitting || !product.trim() || !quantity}
          className="w-full px-6 py-3 bg-gradient-to-r from-indigo-600 to-indigo-500
                   hover:from-indigo-700 hover:to-indigo-600
                   disabled:from-slate-300 disabled:to-slate-300 disabled:cursor-not-allowed
                   text-white font-medium rounded-xl shadow-lg shadow-indigo-500/30
                   hover:shadow-xl hover:shadow-indigo-500/40
                   transition-all duration-200 transform hover:scale-[1.02] active:scale-[0.98]"
        >
          {submitting ? (
            <span className="flex items-center justify-center gap-2">
              <svg className="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"/>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"/>
              </svg>
              Creating...
            </span>
          ) : (
            'Create Order'
          )}
        </button>
      </form>

      <div className="mt-6 pt-6 border-t border-slate-100">
        <p className="text-xs text-slate-500 text-center">
          Orders are processed asynchronously via Kafka
        </p>
      </div>
    </div>
  )
}

export default OrderForm
