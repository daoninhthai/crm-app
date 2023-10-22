import { useState } from 'react';

interface RevenueChartProps {
  data?: { month: string; revenue: number }[];
}

const defaultData = [
  { month: 'Jan', revenue: 45000 },
  { month: 'Feb', revenue: 52000 },
  { month: 'Mar', revenue: 38000 },
  { month: 'Apr', revenue: 61000 },
  { month: 'May', revenue: 55000 },
  { month: 'Jun', revenue: 72000 },
  { month: 'Jul', revenue: 68000 },
  { month: 'Aug', revenue: 81000 },
  { month: 'Sep', revenue: 75000 },
  { month: 'Oct', revenue: 89000 },
  { month: 'Nov', revenue: 93000 },
  { month: 'Dec', revenue: 105000 },
];

export default function RevenueChart({ data = defaultData }: RevenueChartProps) {
  const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);

  const maxRevenue = Math.max(...data.map((d) => d.revenue));
  const chartHeight = 200;
  const barWidth = 40;
  const gap = 16;
  const chartWidth = data.length * (barWidth + gap) - gap + 60;

  const formatCurrency = (value: number): string => {
    if (value >= 1000) {
      return `$${(value / 1000).toFixed(0)}k`;
    }
    return `$${value}`;
  };

  return (
    <div className="card">
      <div className="flex items-center justify-between mb-6">
        <h3 className="text-lg font-semibold text-gray-900">Revenue Overview</h3>
        <span className="text-sm text-gray-500">Last 12 months</span>
      </div>

      <div className="overflow-x-auto">
        <svg width={chartWidth} height={chartHeight + 40} className="mx-auto">
          {/* Y-axis labels */}
          {[0, 0.25, 0.5, 0.75, 1].map((fraction) => {
            const y = chartHeight - fraction * chartHeight;
            const value = maxRevenue * fraction;
            return (
              <g key={fraction}>
                <line
                  x1="50"
                  y1={y}
                  x2={chartWidth}
                  y2={y}
                  stroke="#f3f4f6"
                  strokeWidth="1"
                />
                <text
                  x="45"
                  y={y + 4}
                  textAnchor="end"
                  className="text-xs"
                  fill="#9ca3af"
                  fontSize="10"
                >
                  {formatCurrency(value)}
                </text>
              </g>
            );
          })}

          {/* Bars */}
          {data.map((item, index) => {
            const barHeight = (item.revenue / maxRevenue) * chartHeight;
            const x = 60 + index * (barWidth + gap);
            const y = chartHeight - barHeight;
            const isHovered = hoveredIndex === index;

            return (
              <g
                key={item.month}
                onMouseEnter={() => setHoveredIndex(index)}
                onMouseLeave={() => setHoveredIndex(null)}
                className="cursor-pointer"
              >
                <rect
                  x={x}
                  y={y}
                  width={barWidth}
                  height={barHeight}
                  rx="4"
                  fill={isHovered ? '#4f46e5' : '#818cf8'}
                  className="transition-all duration-200"
                />
                <text
                  x={x + barWidth / 2}
                  y={chartHeight + 18}
                  textAnchor="middle"
                  fontSize="11"
                  fill="#6b7280"
                >
                  {item.month}
                </text>

                {/* Tooltip */}
                {isHovered && (
                  <g>
                    <rect
                      x={x + barWidth / 2 - 35}
                      y={y - 30}
                      width="70"
                      height="24"
                      rx="4"
                      fill="#1f2937"
                    />
                    <text
                      x={x + barWidth / 2}
                      y={y - 14}
                      textAnchor="middle"
                      fontSize="11"
                      fill="white"
                      fontWeight="500"
                    >
                      {formatCurrency(item.revenue)}
                    </text>
                  </g>
                )}
              </g>
            );
          })}
        </svg>
      </div>
    </div>
  );
}
