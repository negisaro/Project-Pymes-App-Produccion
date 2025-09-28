// Fondo animado de partículas para la home premium
(function(){
  const canvas = document.getElementById('bg-particles');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  let w = window.innerWidth, h = window.innerHeight;
  canvas.width = w; canvas.height = h;
  let particles = Array.from({length: 48}).map(()=>({
    x: Math.random()*w,
    y: Math.random()*h,
    r: 1.2+Math.random()*2.8,
    dx: -0.5+Math.random(),
    dy: -0.5+Math.random(),
    o: 0.12+Math.random()*0.18
  }));
  function draw() {
    ctx.clearRect(0,0,w,h);
    for (const p of particles) {
      ctx.beginPath();
      ctx.arc(p.x,p.y,p.r,0,2*Math.PI);
      ctx.fillStyle = `rgba(38,208,206,${p.o})`;
      ctx.shadowColor = '#26d0ce';
      ctx.shadowBlur = 12;
      ctx.fill();
      ctx.shadowBlur = 0;
      p.x += p.dx; p.y += p.dy;
      if (p.x<0||p.x>w) p.dx*=-1;
      if (p.y<0||p.y>h) p.dy*=-1;
    }
    requestAnimationFrame(draw);
  }
  window.addEventListener('resize',()=>{
    w=window.innerWidth; h=window.innerHeight;
    canvas.width=w; canvas.height=h;
  });
  draw();
})();
