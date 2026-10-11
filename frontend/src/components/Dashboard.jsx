function Dashboard({ orders, loading }) {
  const totalOrders = orders.length
  const processedOrders = orders.filter(o => o.status === 'PROCESSED').length
  const processingOrders = orders.filter(o => o.status === 'PROCESSING').length
  const receivedOrders = orders.filter(o => o.status === 'RECEIVED').length

  const stats = [
    {
      label: 'Total Orders',
      value: totalOrders,
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
        </svg>
      ),
      color: 'from-indigo-500 to-indigo-600',
      bg: 'bg-indigo-50',
      textColor: 'text-indigo-700'
    },
    {
      label: 'Completed',
      value: processedOrders,
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      ),
      color: 'from-emerald-500 to-emerald-600',
      bg: 'bg-emerald-50',
      textColor: 'text-emerald-700'
    },
    {
      label: 'In Progress',
      value: processingOrders,
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      ),
      color: 'from-amber-500 to-amber-600',
      bg: 'bg-amber-50',
      textColor: 'text-amber-700'
    },
    {
      label: 'Pending',
      value: receivedOrders,
      icon: (
        <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
      ),
      color: 'from-blue-500 to-blue-600',
      bg: 'bg-blue-50',
      textColor: 'text-blue-700'
    }
  ]

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      {stats.map((stat, index) => (
        <div 
          key={stat.label}
          className="relative bg-white rounded-2xl shadow-sm border border-slate-200 p-6 overflow-hidden
                   hover:shadow-md transition-all duration-200 hover:-translate-y-0.5"
        >
          {/* Background Gradient */}
          <div className={`absolute inset-0 bg-gradient-to-br ${stat.color} opacity-5`}></div>
          
          {/* Content */}
          <div className="relative">
            <div className="flex items-start justify-between mb-4">
              <div className={`w-12 h-12 ${stat.bg} rounded-xl flex items-center justify-center ${stat.textColor}`}>
                {stat.icon}
              </div>
            </div>
            
            <div>
              {loading ? (
                <div className="h-8 w-16 bg-slate-100 rounded animate-pulse"></div>
              ) : (
                <p className="text-3xl font-bold text-slate-900 mb-1">
                  {stat.value}
                </p>
              )}
              <p className="text-sm font-medium text-slate-600">{stat.label}</p>
            </div>
          </div>

          {/* Decorative Element */}
          <div className={`absolute -right-4 -bottom-4 w-24 h-24 bg-gradient-to-br ${stat.color} opacity-10 rounded-full blur-2xl`}></div>
        </div>
      ))}
    </div>
  )
}

export default Dashboard
